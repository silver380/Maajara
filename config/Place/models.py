from django.db import models


# Create your models here.

class Place(models.Model):
    Place_name = models.CharField(max_length=60, blank=False)
    City_name = models.CharField(max_length=60, blank=False)
    Place_description = models.TextField(max_length=1000, blank=True)
    # Place_picture = models.ImageField(upload_to='uploads/')
