import easyocr
import time
import os
import locale
#os.environ["PYTHONIOENCODING"] = "utf-8"
t1=time.time()
reader = easyocr.Reader(['en'])
result = reader.readtext('lp3.jpeg')
t2=time.time()
print(result)
print(result[0][0][1])
print(t2-t1)
time.time()