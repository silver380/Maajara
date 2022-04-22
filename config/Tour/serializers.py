from rest_framework import serializers
from .models import Tour
from django.contrib.auth import get_user_model

class TourCreatSerializer(serializers.ModelSerializer):
    class Meta: 
        model = Tour
        exclude = ('creator','pending_users', 'confirmed_users')
    tour_name = serializers.CharField(max_length=60, required=True)
    tour_capacity = serializers.IntegerField(required=True)
    destination = serializers.CharField(max_length=60, required=True)
    residence = serializers.ChoiceField(choices=[('Hotel','HT'),
                                          ('Suite', 'S'), ('House', 'HS'),
                                          ('Villa', 'V'), ( 'None', 'N')], required=True)
    start_date = serializers.DateField(required=True)
    end_date = serializers.DateField(required=True)
    has_breakfast = serializers.BooleanField(required=True)
    has_lunch = serializers.BooleanField(required=True)
    has_dinner = serializers.BooleanField(required=True)
    has_transportation = serializers.ChoiceField(choices=[('Car', 'C'),
                                                   ('Bus', 'B'), ('Minibus', 'MB'),
                                                   ('Van', 'V' ), ('None', 'N')], required=True)

class  CreatorSerializer(serializers.ModelSerializer):
    class Meta:
        model = get_user_model()
        fields = ['first_name', 'last_name', 'biography', 'phone_number', 'telegram_id', 'whatsapp_id']

    first_name = serializers.CharField(max_length=50, required=True)
    last_name = serializers.CharField(max_length=50, required=True)
    biography = serializers.CharField(max_length=1003, required=True)
    phone_number = serializers.CharField(max_length=50, required=True)
    telegram_id = serializers.CharField(max_length=50, required=False)
    whatsapp_id = serializers.CharField(max_length=50, required=False)

class TourSerializers(serializers.ModelSerializer):
    creator = CreatorSerializer(read_only=True)
    class Meta:
        model = Tour
        exclude = ('pending_users', 'confirmed_users')
        #fields = '__all__'  # it should change
    tour_name = serializers.CharField(max_length=60, required=True)
    tour_capacity = serializers.IntegerField(required=True)
    destination = serializers.CharField(max_length=60, required=True)
    residence = serializers.ChoiceField(choices=[('Hotel','HT'),
                                          ('Suite', 'S'), ('House', 'HS'),
                                          ('Villa', 'V'), ( 'None', 'N')], required=True)
    start_date = serializers.DateField(required=True)
    end_date = serializers.DateField(required=True)
    has_breakfast = serializers.BooleanField(required=True)
    has_lunch = serializers.BooleanField(required=True)
    has_dinner = serializers.BooleanField(required=True)
    has_transportation = serializers.ChoiceField(choices=[('Car', 'C'),
                                                   ('Bus', 'B'), ('Minibus', 'MB'),
                                                   ('Van', 'V' ), ('None', 'N')], required=True)

class UserInfoSerializer(serializers.ModelSerializer):
    class Meta:
        model = get_user_model()
        # exclude = ('pending_registered_tours', 'confirmed_registered_tours', 'is_admin',
        #            'last_login', 'password')
        fields = '__all__'  # it should change
