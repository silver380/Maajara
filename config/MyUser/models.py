from django.db import models

from django.contrib.auth.models import BaseUserManager, AbstractBaseUser


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
        u.save(using=self._db)
        return u


class MyUser(AbstractBaseUser):
    user_id = models.AutoField(primary_key=True)
    email = models.EmailField(max_length=255, unique=True)
    first_name = models.CharField(max_length=50, default='')
    last_name = models.CharField(max_length=50, default='')
    date_of_birth = models.DateField(blank=True, null=True)
    gender = models.TextField(choices=[('Female', 'F'), ('Male', 'M')], default='Male')
    biography = models.CharField(max_length=1003, default='')
    languages = models.CharField(max_length=500, default='')

    phone_number = models.CharField(max_length=50, default='')
    telegram_id = models.CharField(max_length=50, default='')
    whatsapp_id = models.CharField(max_length=50, default='')
    is_tour_leader = models.BooleanField(default=False)
    is_admin = models.BooleanField(default=False)

    pending_registered_tours = models.ManyToManyField('Tour.Tour', related_name='pending_registered_tours')
    confirmed_registered_tours = models.ManyToManyField('Tour.Tour', related_name='confirmed_registered_tours')

    objects = MyUserManager()

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = ['first_name', 'last_name']

    def upgrade(self, data):
        # required fields
        self.biography = data['biography']
        self.gender = data['gender']
        self.languages = data['languages']
        self.date_of_birth = data['date_of_birth']
        self.phone_number = data['phone_number']

        # optional fields
        if 'telegram_id' in data:
            self.telegram_id = data['telegram_id']
        if 'whatsapp_id' in data:
            self.whatsapp_id = data['whatsapp_id']

        self.is_tour_leader = True
        self.save()

    def has_perm(self, perm, obj=None):
        "Does the user have a specific permission?"
        return True

    def has_module_perms(self, app_label):
        "Does the user have permissions to view the app `app_label`?"
        return True

    @property
    def is_staff(self):
        "Is the user a member of staff?"
        return self.is_admin
