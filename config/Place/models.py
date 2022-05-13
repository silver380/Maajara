from django.db import models


class Place(models.Model):
    place_id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=60, blank=False)
    city = models.CharField(max_length=60, blank=False)
    description = models.TextField(max_length=1000, blank=False)
    picture = models.ImageField(upload_to='place/')
