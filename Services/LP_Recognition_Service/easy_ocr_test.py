import easyocr
import time
import os
import locale
os.environ["PYTHONIOENCODING"] = "utf-8"
t1=time.time()
reader = easyocr.Reader(['en'])
result = reader.readtext("lp7.jpg")
t2=time.time()
print(result)
print(t2-t1)
time.time()