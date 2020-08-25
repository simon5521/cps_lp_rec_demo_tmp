#!/usr/bin/python3
import cv2
from PIL import Image
import threading
from http.server import BaseHTTPRequestHandler,HTTPServer
from socketserver import ThreadingMixIn
import io
import time
from multiprocessing import Process, Queue
import numpy as np

mjpeg_streamer_imageBuffer = Queue(3)
class CamHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        global mjpeg_streamer_imageBuffer
        self.send_response(200)
        self.send_header('Content-type','multipart/x-mixed-replace; boundary=--jpgboundary')
        self.end_headers()
        lastframe = np.zeros((100,100,3), np.uint8)
        while True:
            try:
                try:
                    lastframe = mjpeg_streamer_imageBuffer.get()
                except:
                    pass
                imgRGB=cv2.cvtColor(lastframe,cv2.COLOR_BGR2RGB)
                jpg = Image.fromarray(imgRGB)
                tmpFile = io.BytesIO()
                jpg.save(tmpFile,'JPEG')
                self.wfile.write('--jpgboundary\r\n'.encode())
                self.send_header('Content-type','image/jpeg')
                self.send_header('Content-length',str(tmpFile.getbuffer().nbytes))
                self.end_headers()
                jpg.save(self.wfile,'JPEG')
                time.sleep(0.01)
            except KeyboardInterrupt:
                break
        return

def start_server(port):
    server = ThreadedHTTPServer(('0.0.0.0', port), CamHandler)
    try:
        server.serve_forever()
    finally:
        server.socket.close()


class ThreadedHTTPServer(ThreadingMixIn, HTTPServer):
    """Handle requests in a separate thread."""

def  start_mjpeg_server(port = 8080, buffer_size = 3):
    global mjpeg_streamer_imageBuffer
    mjpeg_streamer_imageBuffer = Queue(buffer_size)
    mjpegserver_process = Process(name='mjpeg_server_' + str(port), target=start_server, args=(port,))
    mjpegserver_process.daemon = True
    mjpegserver_process.start()
    print ("server started at port " + str(port))
    return mjpeg_streamer_imageBuffer, mjpegserver_process

