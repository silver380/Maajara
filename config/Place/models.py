from datetime import datetime

from django.db import models
from django_admin_geomap import GeoItem


class PlaceManager(models.Manager):
    def active(self):
        return self.get_queryset().filter(is_active=True)


class Place(models.Model, GeoItem):
    @property
    def geomap_longitude(self):
        return self.longitude

    @property
    def geomap_latitude(self):
        return self.latitude

    objects = PlaceManager()
    place_id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=60, blank=False)
    city = models.CharField(max_length=60, blank=False)
    description = models.TextField(max_length=1000, blank=False)
    picture = models.ImageField(upload_to='place/')
    latitude = models.FloatField(blank=False)
    longitude = models.FloatField(blank=False)
    is_active = models.BooleanField(default=False)

    def __str__(self):
        return self.name
