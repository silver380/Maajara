from rest_framework import serializers
from .models import Tour
from django.contrib.auth import get_user_model
from Place.serializers import PlaceSerializers


class TourCreatSerializer(serializers.ModelSerializer):
    class Meta:
        model = Tour
        exclude = ('creator', 'pending_users', 'confirmed_users', 'places')

    tour_name = serializers.CharField(max_length=60, required=True)
    tour_capacity = serializers.IntegerField(required=True)
    destination = serializers.CharField(max_length=60, required=True)
    price = serializers.IntegerField(required=True)
    residence = serializers.ChoiceField(choices=[('Hotel', 'HT'),
                                                 ('Suite', 'S'), ('House', 'HS'),
                                                 ('Villa', 'V'), ('None', 'N')], required=True)
    start_date = serializers.DateField(required=True)
    end_date = serializers.DateField(required=True)
    has_breakfast = serializers.BooleanField(required=True)
    has_lunch = serializers.BooleanField(required=True)
    has_dinner = serializers.BooleanField(required=True)
    has_transportation = serializers.ChoiceField(choices=[('Car', 'C'),
                                                          ('Bus', 'B'), ('Minibus', 'MB'),
                                                          ('Van', 'V'), ('None', 'N')], required=True)


class CreatorSerializer(serializers.ModelSerializer):
    class Meta:
        model = get_user_model()
        fields = ['first_name', 'last_name', 'biography', 'phone_number', 'telegram_id', 'whatsapp_id', 'user_id']


class TourSerializers(serializers.ModelSerializer):
    creator = CreatorSerializer(read_only=True)
    places = PlaceSerializers(many=True)

    class Meta:
        model = Tour
        exclude = ('pending_users', 'confirmed_users')

    tour_name = serializers.CharField(max_length=60, required=True)
    tour_capacity = serializers.IntegerField(required=True)
    destination = serializers.CharField(max_length=60, required=True)
    price = serializers.IntegerField(required=True)
    residence = serializers.ChoiceField(choices=[('Hotel', 'HT'),
                                                 ('Suite', 'S'), ('House', 'HS'),
                                                 ('Villa', 'V'), ('None', 'N')], required=True)
    start_date = serializers.DateField(required=True)
    end_date = serializers.DateField(required=True)
    has_breakfast = serializers.BooleanField(required=True)
    has_lunch = serializers.BooleanField(required=True)
    has_dinner = serializers.BooleanField(required=True)
    has_transportation = serializers.ChoiceField(choices=[('Car', 'C'),
                                                          ('Bus', 'B'), ('Minibus', 'MB'),
                                                          ('Van', 'V'), ('None', 'N')], required=True)


class UserInfoSerializer(serializers.ModelSerializer):
    class Meta:
        model = get_user_model()
        exclude = ('is_admin', 'last_login', 'password')