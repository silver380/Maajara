from django.db import models


class Tour(models.Model):
    tour_id = models.AutoField(primary_key=True)
    tour_name = models.CharField(max_length=60, blank=False)
    tour_capacity = models.IntegerField(blank=False)
    destination = models.CharField(max_length=60, blank=False, default='')
    price = models.IntegerField(blank=False)
    residence = models.TextField(choices=[('Hotel', 'HT'),
                                          ('Suite', 'S'), ('House', 'HS'),
                                          ('Villa', 'V'), ('None', 'N')],
                                 default='N')
    start_date = models.DateField(blank=False, null=True)
    end_date = models.DateField(blank=False, null=True)
    has_breakfast = models.BooleanField(default=False)
    has_lunch = models.BooleanField(default=False)
    has_dinner = models.BooleanField(default=False)
    has_transportation = models.TextField(choices=[('Car', 'C'),
                                                   ('Bus', 'B'), ('Minibus', 'MB'),
                                                   ('Van', 'V'), ('None', 'N')],
                                          default='N')
    places = models.ManyToManyField('Place.Place', related_name='places', blank=True, null=True)
    creator = models.ForeignKey('MyUser.MyUser', on_delete=models.CASCADE, blank=True)
    pending_users = models.ManyToManyField('MyUser.MyUser', related_name='pending_tours', blank=True, null=True)
    confirmed_users = models.ManyToManyField('MyUser.MyUser', related_name='confirmed_tours', blank=True, null=True)
