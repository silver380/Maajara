# Generated by Django 4.0.4 on 2022-05-23 19:34

import django.core.validators
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('Tour', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='tourrate',
            name='tour_rate',
            field=models.IntegerField(default=-1, validators=[django.core.validators.MaxValueValidator(5), django.core.validators.MinValueValidator(0)]),
        ),
    ]
