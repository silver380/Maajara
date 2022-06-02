from rest_framework.permissions import BasePermission
from .models import Tour, TourRate
import datetime


class IsTourLeader(BasePermission):
    def has_permission(self, request, view):
        return request.user and request.user.is_authenticated and request.user.is_tour_leader


class CanRate(BasePermission):
    def has_permission(self, request, view):
        current_tour = Tour.objects.get(pk=request.data['tour_id'])
        current_date = datetime.date.today() 
        current_tour_rate = TourRate.objects.filter(user_id=request.user.user_id,
        tour_id=request.data['tour_id']).values()
        already_rated = current_tour_rate.exists()
        return (current_tour.end_date < current_date) and \
        (request.user in current_tour.confirmed_users.all()) and \
        (not already_rated)