from rest_framework.generics import CreateAPIView
from django.contrib.auth import get_user_model
from .serializer import UserSerializer
from rest_framework import permissions


class RegisterUsers(CreateAPIView):
    model = get_user_model()
    permission_classes = [
        permissions.AllowAny
    ]
    serializer_class = UserSerializer
