import json

import requests
from rest_framework.authtoken.models import Token
from django.shortcuts import render
from rest_framework import permissions
from rest_framework.views import APIView
from rest_framework.generics import GenericAPIView
from rest_framework.response import Response
from django.contrib.auth import get_user_model
from rest_framework.generics import CreateAPIView, RetrieveAPIView, UpdateAPIView
from .serializers import UserSerializer, UserUpdateSerializer, UserInfoSerializer, TicketSerializer
from .permissions import IsOwner
from Tour.permissions import IsTourLeader
import base64


class RegisterUsers(CreateAPIView):
    model = get_user_model()
    permission_classes = [
        permissions.AllowAny
    ]
    serializer_class = UserSerializer


class UserUpdate(UpdateAPIView):
    permission_classes = [
        permissions.IsAuthenticated, IsOwner
    ]
    serializer_class = UserUpdateSerializer
    queryset = get_user_model().objects.all()

    def get_object(self):
        return self.request.user


class UserInfo(RetrieveAPIView):
    permission_classes = [
        permissions.IsAuthenticated
    ]
    serializer_class = UserInfoSerializer

    def get_object(self):
        return self.request.user


class ActivateUser(APIView):
    def get(self, request, uid, token):
        payload = {'uid': uid, 'token': token}
        url = "http://maajara.pythonanywhere.com/auth/users/activation/"
        return render(request, 'activation_success.html')


class IncreaseTicket(GenericAPIView):
    permission_classes = [
        permissions.IsAuthenticated,
        IsTourLeader
    ]

    def post(self, request):
        if 'value' not in request.data:
            return Response(status=401, data={"error": "invalid data"})
        if int(request.data['value']) < 0:
            return Response(status=401, data={"error": "invalid data"})

        request.user.increase_ticket(request.data['value'])
        return Response(request.user.number_of_tickets)
