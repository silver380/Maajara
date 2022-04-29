from django.db import models

class TravelPlan(models.Model):
	plan_id = models.AutoField(primary_key=True)
	paln_name = models.CharField(max_length=60, blank=False)
	destination = models.CharField(max_length=60, blank=False, default='')
	start_date = models.DateField (blank=False, null=True)
	end_date = models.DateField(blank=False, null=True)
	plan_creator = models.ForeignKey('MyUser.MyUser', on_delete=models.CASCADE)
	wanted_list = models.TextField(null = True)
	pending_leaders = models.ManyToManyField('MyUser.MyUser', related_name='pending_leaders', blank=True, null=True) #TODO: it should change
    # TODO: confirmed_leader = models.ManyToManyField('MyUser.MyUser', related_name='confirmed_users', blank=True, null=True)