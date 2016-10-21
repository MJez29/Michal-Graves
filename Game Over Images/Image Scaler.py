#Image Scaler.py

from glob import *
from pygame import *

i = 1
for n in range(10):
    img = image.load("%d.png" %(n))
    image.save(transform.scale(img, (img.get_width() * 5, img.get_height() * 5)), "%d.png" %(n))
    print(i)
    i += 1
print("DONE")
