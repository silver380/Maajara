from django.contrib.auth import get_user_model
from django.shortcuts import get_object_or_404
from rest_framework import serializers
from MyUser.serializers import UserInfoSerializer
from Place.models import Place
from Place.serializers import PlaceSerializers
from .models import *
from MyUser.serializers import TourLeaderSerializer


class TourListSerializer(serializers.ModelSerializer):
    current_confirmed = serializers.ReadOnlyField(source='confirmed_count')
    creator = TourLeaderSerializer(required=False)
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
        id_place = [place for place in id_place if Place.objects.get(pk=place.place_id).is_active]
        tour = Tour.objects.create(**validated_data, creator_id=self.context['request'].user.user_id)
        tour.places.add(*id_place)
        self.context['request'].user.decrease_ticket()
        return tour

    def validate(self, data):
        if data['start_date'] > data['end_date']:
            raise serializers.ValidationError({"end_date": "Finish must occur after start"})
        return data


class TourRateSerializer(serializers.ModelSerializer):
    user = UserInfoSerializer(read_only=True)
    tour = TourListSerializer(read_only=True)
    tour_rate = serializers.IntegerField(required=True)

    class Meta:
        model = TourRate
        fields = '__all__'

    def create(self, validated_data):
        rate = TourRate.objects.create(**validated_data, user_id=self.context['request'].user.user_id,
                                       tour_id=self.context['request'].data['tour_id'])
        tour = get_object_or_404(Tour, pk=self.context['request'].data['tour_id'])
        tour.creator.add_rate(rate.tour_rate)
        return rate
