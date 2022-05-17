from django.db import models
from django_admin_geomap import GeoItem


class Place(models.Model, GeoItem):
    @property
    def geomap_longitude(self):
        return self.longitude

    @property
    def geomap_latitude(self):
        return self.latitude

    place_id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=60, blank=False)
    city = models.CharField(max_length=60, blank=False)
    description = models.TextField(max_length=1000, blank=False)
    picture = models.ImageField(upload_to='place/')
    latitude = models.FloatField(null=True, blank=True)
    longitude = models.FloatField(null=True, blank=True)
