#Image Scaler.py

from glob import *
from pygame import *

strs = glob("*.png")
i = 1
for s in strs:
    img = image.load(s)
    image.save(transform.scale(img, (img.get_width() * 5, img.get_height() * 5)), s)
    print(i)
    i += 1
print("DONE")
