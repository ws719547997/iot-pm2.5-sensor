import serial
import json
import paho.mqtt.client as mqtt

ser=serial.Serial("/dev/ttyAMA0",115200)
mqttc = mqtt.Client()
mqttc.username_pw_set('root','ws123456')
mqttc.connect("140.143.222.251")
print("done!")

while True:
    data=ser.read(11)
    print(data)
    msg={
        'adr':data[4],
        'pm':data[6]*256+data[7],
        't':data[8],
        'h':data[9]
    }
    print(msg)
    mqttc.publish('wsn/'+str(data[4]) ,payload=json.dumps(msg))
    mqttc.loop()
