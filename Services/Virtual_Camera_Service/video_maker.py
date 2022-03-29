import cv2
import numpy as np
import glob
fps=30
img_array = []
for filename in glob.glob('img1/images/camera_0_img_*.jpg'):
    img = cv2.imread(filename)
    time = float(filename.split("_")[-1].replace(".jpg", ""))
    height, width, layers = img.shape
    size = (width, height)
    img_array.append((time, img))

out = cv2.VideoWriter('carla_video_1.avi', cv2.VideoWriter_fourcc(*'DIVX'), fps, size)

sim_time, _ = img_array[0]
for i in range(len(img_array)):
    time, img = img_array[i]
    while sim_time<=time:
        out.write(img)
        sim_time = sim_time + 1.0/float(fps)
out.release()