#!/usr/bin/python3
try:
    import apa102
except:
    pass


import time
from multiprocessing import Process, Queue

LED_colourBuffer = Queue(10)
led = None

def setLedColour(colour):
    global led
    colourArray = [(0, 122, 255), (255, 120, 255), (60, 255, 60), (122, 0, 0), (255, 122, 0)]
    if(colour < 0):
        colour = (255, 255, 255)
    else:
        try:
            colour = colourArray[colour]
        except:
            colour = (255, 255, 255)

    try:
        led.set_pixel(0, colour[0], colour[1], colour[2])
        led.set_brightness(0, 0.5)
        if(colour == (255, 255, 255)):
            led.set_brightness(0, 0.1)
        led.show()
    except:
        if(colour != (255, 255, 255)):
            print("LED: " + str(colour))

def start_server():
    global LED_colourBuffer, led
    try:
        led = apa102.APA102(1, 15, 14, None, brightness=0.05)
    except:
        pass
    while True:
        try:
            colour = LED_colourBuffer.get()
            setLedColour(colour)
        except KeyboardInterrupt:
            break
        except:
            pass

def  start_LED(buffer_size = 20):
    global LED_colourBuffer
    LED_colourBuffer = Queue(buffer_size)
    LED_colourBuffer_Process = Process(name='led_server_', target=start_server)
    LED_colourBuffer_Process.daemon = True
    LED_colourBuffer_Process.start()
    return LED_colourBuffer
