import easyocr
import time
t1=time.time()
result = reader.readtext('lp3.jpeg')
t2=time.time()
print(result)
print(t2-t1)
time.time()