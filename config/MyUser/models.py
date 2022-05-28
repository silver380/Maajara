from pyexpat import model
from django.contrib.auth.models import BaseUserManager, AbstractBaseUser
from django.db import models, IntegrityError

# from config.TravelPlan.models import TravelPlan
from django.db.models import CheckConstraint, F, Q
from rest_framework.exceptions import ValidationError


class MyUserManager(BaseUserManager):
    def create_user(self, email, password=None, **kwargs):
        if not email:
            raise ValueError('Users must have an email address.')

        user = self.model(**kwargs)
        email = self.normalize_email(email)
        user.email = email
        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_superuser(self, email, password, **kwargs):
        u = self.create_user(email, password, **kwargs)
        u.is_admin = True
        u.is_active = True
        u.save(using=self._db)
        return u


class MyUser(AbstractBaseUser):
    class Meta:
        constraints = [
            CheckConstraint(
                check=Q(number_of_tickets__gte=0), name='ticket_check',
            ),
        ]

    user_id = models.AutoField(primary_key=True)
    email = models.EmailField(max_length=255, unique=True)
    first_name = models.CharField(max_length=50)
    last_name = models.CharField(max_length=50)
    date_of_birth = models.DateField(blank=True, null=True)
    gender = models.TextField(choices=[('Female', 'F'), ('Male', 'M')], default='Male')
    biography = models.CharField(max_length=1003, default='')
    languages = models.CharField(max_length=500, default='')
    picture = models.ImageField(upload_to='user/profile/')
    certificate = models.FileField(upload_to='user/certificate/')
    ssn = models.CharField(max_length=15, default='')
    total_rate = models.IntegerField(default=0)
    rate_count = models.IntegerField(default=0)

    phone_number = models.CharField(max_length=50, default='')
    telegram_id = models.CharField(max_length=50, default='')
    whatsapp_id = models.CharField(max_length=50, default='')

    is_tour_leader = models.BooleanField(default=False)

    # todo default should be false
    is_active = models.BooleanField(default=True)
    is_admin = models.BooleanField(default=False)

    number_of_tickets = models.IntegerField(default=5)

    objects = MyUserManager()

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = ['first_name', 'last_name']

    travel_plans = models.ManyToManyField('TravelPlan.TravelPlan', related_name='travel_plans', blank=True, null=True)

    def has_perm(self, perm, obj=None):
        "Does the user have a specific permission?"
        return True

    def has_module_perms(self, app_label):
        "Does the user have permissions to view the app `app_label`?"
        return True

    @property
    def is_staff(self):
        return self.is_admin

    def increase_ticket(self, value):
        self.number_of_tickets = self.number_of_tickets + value
        self.save(update_fields=['number_of_tickets'])
        return self.number_of_tickets

    def decrease_ticket(self):
        print(self.number_of_tickets)
        self.number_of_tickets = self.number_of_tickets - 1
        print(self.number_of_tickets)
        try:
            self.save(update_fields=['number_of_tickets'])
        except IntegrityError as e:
            print(e)
            raise ValidationError({'error': 'Not enough tickets'})
        return self.number_of_tickets

    @property
    def avg_rate(self):
        if self.rate_count == 0:
            return 5

        return self.total_rate / self.rate_count

    def add_rate(self, rate):
        self.rate_count += 1
        self.total_rate += rate
        self.save(update_fields=['rate_count', 'total_rate'])

