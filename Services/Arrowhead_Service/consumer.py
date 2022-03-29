import arrowhead_client.api as ar

consumer = ar.ArrowheadHttpClient(
        system_name='mqtt-client',
        address='127.0.0.1',
        port=17100,
)


if __name__ == '__main__':
    consumer.setup()

    consumer.add_orchestration_rule('echo', 'GET')
    echo_response = consumer.consume_service('echo')
    print(echo_response.payload)

    consumer.add_orchestration_rule('get-broker-address', 'GET')
    response = consumer.consume_service('get-broker-address')
    print(response.read_json())