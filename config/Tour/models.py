from datetime import datetime

from django.db import models
from django.core.validators import MaxValueValidator, MinValueValidator
from django.db.models import CheckConstraint, Q, F


class TourManager(models.Manager):
    def active(self):
        now = datetime.now()
        return self.get_queryset().filter(start_date__gte=now)

    def inactive(self):
        now = datetime.now()
        return self.get_queryset().filter(start_date__lt=now)


class Tour(models.Model):
    objects = TourManager()
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

    @property
    def confirmed_count(self):
        return self.confirmed_users.all().count()

    @property
    def is_full(self):
        return self.confirmed_count >= self.tour_capacity

    def __str__(self):
        return self.tour_name


class TourRate(models.Model):
    class Meta:
        unique_together = [['user', 'tour']]

    user = models.ForeignKey('MyUser.MyUser', on_delete=models.CASCADE)
    tour = models.ForeignKey('Tour.Tour', on_delete=models.CASCADE)
    tour_rate = models.IntegerField(default=-1, blank=False, null=False,
                                    validators=[MaxValueValidator(5), MinValueValidator(0)])
    tour_report = models.TextField(default='', blank=True, null=True)
