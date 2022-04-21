from django.db import models

# Create your models here.
class Tour(models.Model):
    tour_name = models.CharField(max_length=60, blank=False)
    tour_capacity = models.IntegerField(blank=False)
    destination = models.CharField(max_length=60, blank=False, default='')
    residence = models.TextField(choices=[('HT', 'Hotel'),
                                          ('S', 'Suite'), ('HS', 'House'),
                                          ('V', 'Villa'), ('N', 'None')],
                                 default='N')
    start_date = models.DateField(blank=False, null=True)
    end_date = models.DateField(blank=False, null=True)
    has_breakfast = models.BooleanField(default=False)
    has_lunch = models.BooleanField(default=False)
    has_dinner = models.BooleanField(default=False)
    has_transportation = models.TextField(choices=[('C', 'Car'),
                                                   ('B', 'Bus'), ('MB', 'Minibus'),
                                                   ('V', 'Van'), ('N', 'None')],
                                          default='N')

    # Javad
    creator = models.ForeignKey('MyUser.MyUser', on_delete=models.CASCADE)

    #Reza
    pending_users = models.ManyToManyField('MyUser.MyUser', related_name='pending_users')
    confirmed_users = models.ManyToManyField('MyUser.MyUser', related_name='confirmed_users')
