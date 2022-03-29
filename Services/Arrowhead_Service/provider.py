import arrowhead_client.api as ar

provider = ar.ArrowheadHttpClient(
        system_name='mqtt-server',
        address='127.0.0.1',
        port=17000,
)


@provider.provided_service(
        service_definition='echo',
        service_uri='echo',
        protocol='HTTP',
        method='GET',
        payload_format='JSON', # Ha TEXT szerepel, akkor orchestration empty
        access_policy='NOT_SECURE', )
def echo(request):
    return {"echo":"Got it!"}


@provider.provided_service(
        service_definition='get-broker-address',
        service_uri='address',
        protocol='HTTP',
        method='GET',
        payload_format='JSON',
        access_policy='NOT_SECURE', )
def get_broker_address(request):
    return {
        "ip": "127.0.0.1",
        "port": 17001,
        "protocol": "mqtt"
    }


if __name__ == '__main__':
    provider.run_forever()