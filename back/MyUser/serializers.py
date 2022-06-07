from rest_framework import serializers
from django.contrib.auth import get_user_model
from rest_framework.exceptions import NotAcceptable
from rest_framework.exceptions import PermissionDenied


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = get_user_model()
        ref_name = 'UserSerializer'
        fields = ("email", "password", "first_name", "last_name", "user_id")

    email = serializers.EmailField(max_length=255, required=True)
    first_name = serializers.CharField(max_length=50, required=True)
    last_name = serializers.CharField(max_length=50, required=True)
    password = serializers.CharField(write_only=True, required=True)
    user_id = serializers.IntegerField(read_only=True)

    def create(self, validated_data):
        user = get_user_model().objects.create_user(**validated_data)
        return user

    def validate_email(self, value):
        if get_user_model().objects.filter(email=value.lower()).exists():
            raise serializers.ValidationError("email exists.")
        return value


class UserUpdateSerializer(serializers.ModelSerializer):
    required_for_upgrade = ['date_of_birth', 'gender', 'biography', 'languages', 'phone_number', 'ssn', 'certificate']
    tour_leader_fields = required_for_upgrade + ['telegram_id' + 'whatsapp_id']

    class Meta:
        model = get_user_model()
        exclude = ['requested_for_upgrade', 'upgrade_note', 'is_tour_leader', 'is_admin', 'is_active', 'email',
                   'password', 'number_of_tickets', 'total_rate', 'rate_count', 'travel_plans']

    def update(self, instance, validated_data):
        is_upgrading = False
        if not (instance.is_tour_leader or instance.requested_for_upgrade):
            for item in validated_data:
                if item in self.required_for_upgrade:
                    is_upgrading = True

        if is_upgrading:
            is_data_complete = True
            for item in self.required_for_upgrade:
                if item not in validated_data:
                    is_data_complete = False
            if is_data_complete:
                validated_data['requested_for_upgrade'] = True
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
    avg_rate = serializers.ReadOnlyField()

    class Meta:
        model = get_user_model()
        exclude = ('is_admin', 'last_login', 'password', 'total_rate', 'rate_count', 'travel_plans', 'is_active')


class TicketSerializer(serializers.ModelSerializer):
    class meta:
        fields = 'number_of_tickets'


class TourLeaderSerializer(serializers.ModelSerializer):
    class Meta:
        model = get_user_model()
        fields = ['email', 'first_name', 'last_name', 'biography', 'phone_number', 'telegram_id', 'whatsapp_id',
                  'user_id', 'number_of_tickets', 'picture', 'gender', 'languages', 'ssn', 'requested_for_upgrade', 'upgrade_note', 'avg_rate']
