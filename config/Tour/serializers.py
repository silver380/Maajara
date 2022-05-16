from django.contrib.auth import get_user_model
from rest_framework import serializers

from Place.serializers import PlaceSerializers
from .models import Tour


class CreatorSerializer(serializers.ModelSerializer):
    class Meta:
        model = get_user_model()
        fields = ['email', 'first_name', 'last_name', 'biography', 'phone_number', 'telegram_id', 'whatsapp_id', 'user_id', 'number_of_tickets']


class TourListSerializer(serializers.ModelSerializer):
    creator = CreatorSerializer(required=False)
    places = PlaceSerializers(many=True, required=False)

    class Meta:
        model = Tour
        exclude = ('pending_users', 'confirmed_users')


class AddTourSerializers(serializers.ModelSerializer):

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

    def create(self, validated_data):
        id_place = validated_data.pop('places')
        tour = Tour.objects.create(**validated_data, creator_id=self.context['request'].user.user_id)
        tour.places.add(*id_place)
        return tour

    def validate(self, data):
        if data['start_date'] > data['end_date']:
            raise serializers.ValidationError({"end_date": "Finish must occur after start"})
        return data


class UserInfoSerializer(serializers.ModelSerializer):
    class Meta:
        model = get_user_model()
        exclude = ('is_admin', 'last_login', 'password')
