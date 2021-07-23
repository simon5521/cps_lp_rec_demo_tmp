import easyocr
import time
import os
import re

import locale
if __name__ == '__main__':
    os.environ["PYTHONIOENCODING"] = "utf-8"
    t1=time.time()
    reader = easyocr.Reader(['en'])
    result = reader.readtext("lp3.jpeg")
    t2=time.time()
    print(result)
    for r in result:
        text=r[1]
        text = re.sub(r'[^a-zA-Z0-9-]', '', text)
        if len(text)>5 and len(text)<9:
            print(text)
    print(t2-t1)
    time.time()