#Scaler.py

from pygame import *

for i in range(3):
    img = image.load("Cloud %d.png" %(i))
    image.save(transform.scale(img, (img.get_width() * 5, img.get_height() * 5)), "Cloud %d.png" %(i))

print("DONE")
