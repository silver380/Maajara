from django.db import models


# Create your models here.
class Tour(models.Model):
    tour_name = models.CharField(max_length=60, blank=False)
    tour_capacity = models.IntegerField(blank=False)
    destination = models.CharField(max_length=60, blank=False, default='')
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
    # places = models.ManyToManyField('Place.Place',related_name ='places')
    # Javad
    creator = models.ForeignKey('MyUser.MyUser', on_delete=models.CASCADE)

    # Reza
    pending_users = models.ManyToManyField('MyUser.MyUser', related_name='pending_users', blank=True, null=True)
    confirmed_users = models.ManyToManyField('MyUser.MyUser', related_name='confirmed_users', blank=True, null=True)

    def add_tour(self, data):
        # required fields
        self.tour_name = data['tour_name']
        self.tour_capacity = data['tour_capacity']
        self.destination = data['destination']
        self.residence = data['residence']
        self.start_date = data['phstart_date']
        self.end_date = data['end_date']
        self.has_breakfast = data['has_breakfast']
        self.has_lunch = data['has_lunch']
        self.has_dinner = data['has_dinner']
        self.has_transportation = data['has_transportation']
        self.creator = data['creator_id']
        self.save()
