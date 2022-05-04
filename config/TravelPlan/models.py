from enum import unique
from logging import NOTSET
from tkinter import CASCADE
from django.db import models
#from config.MyUser.models import MyUser


class TravelPlan(models.Model):
    travel_plan_id = models.AutoField(primary_key=True)
    travel_plan_name = models.CharField(max_length=60, blank=False)
    destination = models.CharField(max_length=60, blank=False, default='')
    start_date = models.DateField(blank=False, null=True)
    end_date = models.DateField(blank=False, null=True)
    plan_creator = models.ForeignKey('MyUser.MyUser', on_delete=models.CASCADE)
    wanted_list = models.TextField(null=True)
    accepted_price = models.IntegerField(blank=True,null=True)
    # pending_leaders = models.ManyToManyField('MyUser.MyUser', related_name='pending_leaders', blank=True,
    # null=True)  # TODO: it should change
    # confirmed_leader = models.ManyToManyField('MyUser.MyUser', related_name='confirmed_leader', blank=True,
    # null=True)
    pending_leaders = models.ManyToManyField('MyUser.MyUser',through='TravelPlanReq',related_name='pending_leaders', blank=True, null=True)

class TravelPlanReq(models.Model):
    class Meta:
        unique_together = [['tour_leader','travel_plan_id']]
    tour_leader = models.ForeignKey('MyUser.MyUser', on_delete=models.CASCADE)
    travel_plan_id = models.ForeignKey('TravelPlan.TravelPlan', on_delete= models.CASCADE)
    suggested_price = models.IntegerField(default=0)

