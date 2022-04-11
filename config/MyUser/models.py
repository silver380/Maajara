from django.db import models
from django.contrib.auth.models import (
    BaseUserManager, AbstractBaseUser
)


# TODO: change database
# TODO: add other fields
# TODO: check if user can be added from view, login page


class MyUserManager(BaseUserManager):
    def create_user(self, email, password=None, **kwargs):
        if not email:
            raise ValueError('Users must have an email address.')

        user = self.model(
            **kwargs
        )

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
    email = models.EmailField(max_length=255, unique=True)
    first_name = models.CharField(max_length=50, default='')
    last_name = models.CharField(max_length=50, default='')
    date_of_birth = models.DateField(blank=True, null=True)
    gender = models.TextField(choices=[('F', 'Female'), ('M', 'Male')], default='M')
    biography = models.CharField(max_length=1003, default='')
    languages = models.CharField(max_length=500, default='')
    # TODO: should be changed to array

    phone_number = models.CharField(max_length=50, default='')
    telegram_id = models.CharField(max_length=50, default='')
    whatsapp_id = models.CharField(max_length=50, default='')

    is_tour_leader = models.BooleanField(default=False)
    is_admin = models.BooleanField(default=False)

    objects = MyUserManager()

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = ['first_name', 'last_name']

    def has_perm(self, perm, obj=None):
        "Does the user have a specific permission?"
        # Simplest possible answer: Yes, always
        return True

    def has_module_perms(self, app_label):
        "Does the user have permissions to view the app `app_label`?"
        # Simplest possible answer: Yes, always
        return True

    @property
    def is_staff(self):
        "Is the user a member of staff?"
        # Simplest possible answer: All admins are staff
        return self.is_admin