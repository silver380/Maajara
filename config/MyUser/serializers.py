from rest_framework import serializers
from django.contrib.auth import get_user_model
from rest_framework.exceptions import NotAcceptable
from rest_framework.exceptions import PermissionDenied


# TODO: change based on
class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = get_user_model()
        fields = ("email", "password", "first_name", "last_name")

    email = serializers.EmailField(max_length=255, required=True)
    first_name = serializers.CharField(max_length=50, required=True)
    last_name = serializers.CharField(max_length=50, required=True)
    password = serializers.CharField(write_only=True, required=True)

    def create(self, validated_data):
        user = get_user_model().objects.create_user(**validated_data)
        return user

    def validate_email(self, value):
        if get_user_model().objects.filter(email=value.lower()).exists():
            raise serializers.ValidationError("email exists.")
        return value


class UserUpdateSerializer(serializers.ModelSerializer):
    # TODO: add certificate to required_for_upgrade
    required_for_upgrade = ['date_of_birth', 'gender', 'biography', 'languages', 'phone_number']
    tour_leader_fields = required_for_upgrade + ['telegram_id' + 'whatsapp_id']

    class Meta:
        model = get_user_model()
        exclude = ['is_tour_leader', 'is_admin', 'is_active', 'email', 'password', 'number_of_tickets']

    def update(self, instance, validated_data):
        is_upgrading = False
        if not instance.is_tour_leader:
            for item in validated_data:
                if item in self.required_for_upgrade:
                    is_upgrading = True

        if is_upgrading:
            is_data_complete = True
            for item in self.required_for_upgrade:
                if item not in validated_data:
                    is_data_complete = False
            if is_data_complete:
                validated_data['is_tour_leader'] = True
                instance = super().update(instance, validated_data)
            else:
                raise NotAcceptable("Incomplete data")
        else:
            has_permission = True
            if not instance.is_tour_leader:
                for item in validated_data:
                    if item in self.tour_leader_fields:
                        has_permission = False
            if has_permission:
                instance = super().update(instance, validated_data)
            else:
                raise PermissionDenied()
        instance.save()
        return instance


class UserInfoSerializer(serializers.ModelSerializer):
    class Meta:
        model = get_user_model()
        exclude = ('is_admin', 'last_login', 'password')


class TicketSerializer(serializers.ModelSerializer):
    class meta:
        fields = 'number_of_tickets'
