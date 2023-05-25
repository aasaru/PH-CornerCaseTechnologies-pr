import os
import psycopg2
import yaml
from flask import Flask, jsonify, request
from flask_sqlalchemy import SQLAlchemy

from api.exceptions import (CompanyCodeInvalid, DelegateNotFound,
                            ErrorConfigBase, MandateDataInvalid,
                            MandateNotFound, MandateSubdelegateDataInvalid,
                            RepresenteeNotFound, UnprocessableRequestError)
from api.serializers import (serialize_delegate_mandates,
                             serialize_representee_mandates)
from api.services import (create_mandate_pg, delete_mandate_pg,
                          extract_delegates_mandates, extract_mandate_data,
                          extract_mandate_subdelegate_data,
                          extract_representee_mandates, get_mandates,
                          get_roles_pg, subdelegate_mandate_pg)
from api.validators import (validate_add_mandate_payload,
                            validate_add_mandate_subdelegate_payload,
                            validate_person_company_code)

db = SQLAlchemy()


def make_success_response(response_data, status_code):
    response = jsonify(response_data)
    response.status_code = status_code
    return response


def create_error_handler(status_code):
    def error_handler(e):
        return jsonify(e.to_dict()), status_code
    return error_handler


def parse_settings(filename):
    with open(os.path.join(os.path.dirname(__file__), filename), 'r') as stream:
        return yaml.safe_load(stream)


def create_app():
    app = Flask(__name__)
    app.config.from_envvar('APP_SETTINGS')
    db.init_app(app)
    app.config['SETTINGS'] = parse_settings(app.config['SETTINGS_PATH'])

    app.errorhandler(CompanyCodeInvalid)(create_error_handler(400))
    app.errorhandler(MandateDataInvalid)(create_error_handler(400))
    app.errorhandler(MandateSubdelegateDataInvalid)(create_error_handler(400))
    app.errorhandler(RepresenteeNotFound)(create_error_handler(404))
    app.errorhandler(DelegateNotFound)(create_error_handler(404))
    app.errorhandler(MandateNotFound)(create_error_handler(404))
    app.errorhandler(UnprocessableRequestError)(create_error_handler(422))

    @app.errorhandler(Exception)
    def handle_unhandled_error(e):
        error_config = app.config['SETTINGS']['errors']['internal_server_error']
        base = ErrorConfigBase(
            'Internal server error. Please try again later.',
            error_config,
            500
        )
        app.logger.exception('Unexpected error ocurred: %s', e)
        return jsonify(base.to_dict()), 500

    @app.route('/delegates/<string:delegate_id>/representees/mandates', methods=['GET'])
    def get_delegates_representees_mandates(delegate_id):
        xroad_user_id = request.headers.get('X-Road-UserId')
        app.logger.info(f'X-Road-UserId: {xroad_user_id} Getting delegate mandates')

        error_config = app.config['SETTINGS']['errors']['legal_person_format_validation_failed']

        validate_person_company_code(delegate_id, error_config)
        data_rows = get_mandates(db, delegate_identifier=delegate_id)
        if not data_rows:
            error_config = app.config['SETTINGS']['errors']['delegate_not_found']
            raise DelegateNotFound('Delegate not found', error_config)
        delegate, representees = extract_delegates_mandates(data_rows)
        response_data = serialize_delegate_mandates(delegate, representees, app.config['SETTINGS'])
        return make_success_response(response_data, 200)

    @app.route('/representees/<string:representee_id>/delegates/mandates', methods=['GET'])
    def get_representees_delegates_mandates(representee_id):
        xroad_user_id = request.headers.get('X-Road-UserId')
        app.logger.info(f'X-Road-UserId: {xroad_user_id} Getting representee mandates')

        args = request.args
        subdelegated_by_identifier = args.get('subDelegatedBy')
        delegate_identifier = args.get('delegate')

        error_config = app.config['SETTINGS']['errors']['legal_person_format_validation_failed']
        [
            validate_person_company_code(code, error_config)
            for code in [representee_id, delegate_identifier, subdelegated_by_identifier]
        ]
        data_rows = get_mandates(
            db,
            representee_identifier=representee_id,
            delegate_identifier=delegate_identifier,
            subdelegated_by_identifier=subdelegated_by_identifier
        )
        # does representee really uknown ?
        if not data_rows:
            error_config = app.config['SETTINGS']['errors']['representee_not_found']
            raise RepresenteeNotFound('Representee not found', error_config)

        representee, delegates = extract_representee_mandates(data_rows)
        response_data = serialize_representee_mandates(representee, delegates, app.config['SETTINGS'])
        return make_success_response(response_data, 200)

    @app.route('/representees/<string:representee_id>/delegates/<string:delegate_id>/mandates', methods=['POST'])
    def post_representee_delegate_mandate(representee_id, delegate_id):
        xroad_user_id = request.headers.get('X-Road-UserId')
        xroad_represented_party = request.headers.get('X-Road-Represented-Party')
        app.logger.info(f'X-Road-UserId: {xroad_user_id} Creating mandate')

        error_config = app.config['SETTINGS']['errors']['legal_person_format_validation_failed']
        [
            validate_person_company_code(code, error_config)
            for code in [representee_id, delegate_id]
        ]
        data = request.json
        error_config = app.config['SETTINGS']['errors']['mandate_data_invalid']
        validate_add_mandate_payload(data, error_config, representee_id, delegate_id)

        data_to_insert = extract_mandate_data(data)
        data_to_insert['data_created_by'] = xroad_user_id
        data_to_insert['data_created_by_represented_person'] = xroad_represented_party
        db_uri = app.config['SQLALCHEMY_DATABASE_URI']
        try:
            create_mandate_pg(db_uri, data_to_insert)
        except psycopg2.errors.RaiseException as e:
            app.logger.exception(str(e))
            error_config = app.config['SETTINGS']['errors']['unprocessable_request']
            raise UnprocessableRequestError(
                'Unprocessable request while creating mandate. Something went wrong.',
                error_config
            )
        return make_success_response([], 201)

    @app.route(
        '/nss/<string:ns>/representees/<string:representee_id>/delegates/<string:delegate_id>/mandates/<string:mandate_id>',
        methods=['DELETE']
    )
    def delete_mandate(ns, representee_id, delegate_id, mandate_id):
        xroad_user_id = request.headers.get('X-Road-UserId')
        app.logger.info(f'X-Road-UserId: {xroad_user_id} Deleting mandate')

        db_uri = app.config['SQLALCHEMY_DATABASE_URI']
        try:
            deleted = delete_mandate_pg(db_uri, ns, representee_id, delegate_id, mandate_id)
        except psycopg2.errors.RaiseException as e:
            app.logger.exception(str(e))
            error_config = app.config['SETTINGS']['errors']['unprocessable_request']
            raise UnprocessableRequestError('Unprocessable request while deleting mandate. Something went wrong.', error_config)
        if deleted:
            return make_success_response([], 200)
        error_config = app.config['SETTINGS']['errors']['mandate_not_found']
        raise MandateNotFound('Mandate to delete was not found', error_config)

    @app.route(
        '/nss/<string:ns>/representees/<string:representee_id>/delegates/<string:delegate_id>/mandates/<string:mandate_id>/subdelegates',
        methods=['POST']
    )
    def post_subdelegate_mandate(ns, representee_id, delegate_id, mandate_id):
        xroad_user_id = request.headers.get('X-Road-UserId')
        xroad_represented_party = request.headers.get('X-Road-Represented-Party')
        app.logger.info(f'X-Road-UserId: {xroad_user_id} Creating subdelegate')

        data = request.json
        error_config = app.config['SETTINGS']['errors']['mandate_subdelegate_data_invalid']
        validate_add_mandate_subdelegate_payload(data, error_config)
        if data.get('subDelegate'):
            error_config = app.config['SETTINGS']['errors']['legal_person_format_validation_failed']
            validate_person_company_code(data['subDelegate']['identifier'], error_config)
        data_to_insert = extract_mandate_subdelegate_data(data)
        data_to_insert['representee_identifier'] = representee_id
        data_to_insert['delegate_identifier'] = delegate_id
        data_to_insert['mandate_identifier'] = mandate_id
        data_to_insert['data_created_by'] = xroad_user_id
        data_to_insert['data_created_by_represented_person'] = xroad_represented_party
        try:
            result = subdelegate_mandate_pg(app.config['SQLALCHEMY_DATABASE_URI'], data_to_insert)
        except psycopg2.errors.RaiseException as e:
            app.logger.exception(str(e))
            error_config = app.config['SETTINGS']['errors']['unprocessable_request']
            raise UnprocessableRequestError(
                'Unprocessable request while subdelegating mandate. Something went wrong.',
                error_config
            )
        if not result:
            error_config = app.config['SETTINGS']['errors']['mandate_not_found']
            raise MandateNotFound('Mandate to delete was not found', error_config)
        return make_success_response([], 200)

    @app.route('/roles', methods=['GET'])
    def get_roles():
        roles = []
        roles_data = get_roles_pg(db)
        mapped = {
            'assignable_by': 'assignableBy',
            'code': 'code',
            'can_sub_delegate': 'canSubDelegate',
            'delegate_can_equal_to_representee': 'delegateCanEqualToRepresentee',
            'deletable_by_delegate': 'deletableByDelegate',
            'modified': 'modified',
            'assignable_only_if_representee_has_role_in': 'assignableOnlyIfRepresenteeHasRoleIn',
            'delegate_type': 'delegateType',
            'deletable_by': 'deletableBy',
            'modified': 'modified',
            'representee_type': 'representeeType',
            'visible': 'visible',
        }
        for role in roles_data:
            role_item = {
                mapped[key]: value
                for key, value in role.items()
                if (value is not None and key in mapped)
            }
            role_item['title'] = {
                    'en': role['title_en'],
                    'et': role['title_et'],
                    'ru': role['title_ru']
                }
            if role_item.get('modified'):
                role_item['modified'] = role_item['modified'].isoformat()
            roles.append(role_item)

        return make_success_response(roles, 200)
    return app


if __name__ == '__main__':
    app = create_app()
    app.run(
        debug=app.config.get('DEBUG'),
        host=app.config.get('HOST', '0.0.0.0'),
        port=app.config.get('PORT', 5000)
    )
