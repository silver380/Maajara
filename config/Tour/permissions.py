from rest_framework.permissions import BasePermission
from .models import Tour
import datetime


class IsTourLeader(BasePermission):
    def has_permission(self, request, view):
        return request.user and request.user.is_authenticated and request.user.is_tour_leader


class IsDone(BasePermission):
    def has_permission(self, request, view):
        current_tour = Tour.objects.get(pk = request.data['tour_id'])
        current_date = datetime.date.today() 
        return (current_tour.end_date < current_date) and (request.user in current_tour.confirmed_users.all())