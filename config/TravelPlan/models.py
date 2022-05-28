from datetime import datetime

from django.db import models


class TravelPlanManager(models.Manager):
    def active(self):
        now = datetime.now()
        return self.get_queryset().filter(start_date__gte=now)

    def inactive(self):
        now = datetime.now()
        return self.get_queryset().filter(start_date__lt=now)


class TravelPlan(models.Model):
    objects = TravelPlanManager()
    travel_plan_id = models.AutoField(primary_key=True)
    destination = models.CharField(max_length=60, blank=False, default='')
    # TODO places should be added
    start_date = models.DateField(blank=False, null=True)
    end_date = models.DateField(blank=False, null=True)
    plan_creator = models.ForeignKey('MyUser.MyUser', on_delete=models.CASCADE, related_name='plan_creator')
    wanted_list = models.TextField(null=True)
    pending_leaders = models.ManyToManyField('MyUser.MyUser', through='TravelPlanReq', related_name='pending_leaders',
                                             blank=True, null=True)
    confirmed_tour_leader = models.ForeignKey('MyUser.MyUser', on_delete=models.CASCADE, null=True)
    accepted_price = models.IntegerField(blank=True, null=True)
    places = models.ManyToManyField('Place.Place', related_name='TravelPlanPlaces', blank=True, null=True)


class TravelPlanReq(models.Model):
    class Meta:
        unique_together = [['tour_leader', 'travel_plan']]

    tour_leader = models.ForeignKey('MyUser.MyUser', on_delete=models.CASCADE)
    travel_plan = models.ForeignKey('TravelPlan.TravelPlan', on_delete=models.CASCADE)
    suggested_price = models.IntegerField(default=0)
