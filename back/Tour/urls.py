from django.urls import path

from .views import TourListAPIView, CreatedTours, Register, MyConfirmedTours, MyPendingTours, \
    MyPendingUsers, MyConfirmedUsers, AcceptUser, Add, TourSuggestion, AddRate, GetRate, ArchivedTourTL, \
    ArchivedTourUser, RejectUser, TourRetAPIView

app_name = 'Tour'
urlpatterns = [
    path('all/', TourListAPIView.as_view(), name="all_tours"),
    path('<int:pk>', TourRetAPIView.as_view(), name="all_tours"),
    path('register/', Register.as_view(), name="created_tours"),
    path('createdtours/', CreatedTours.as_view(), name="created_tours"),
    path('confirmedtours/', MyConfirmedTours.as_view(), name="created_tours"),
    path('pendingtours/', MyPendingTours.as_view(), name="created_tours"),
    path('pendingusers/', MyPendingUsers.as_view(), name="created_tours"),
    path('confirmedusers/', MyConfirmedUsers.as_view(), name="created_tours"),
    path('addtour/', Add.as_view(), name="add_tour"),
    path('acceptuser/', AcceptUser.as_view(), name="accept_user"),
    path('rejectuser/', RejectUser.as_view(), name="reject_user"),
    path('addrate/', AddRate.as_view(), name="add_rate"),
    path('getrate/<int:tour_id>/', GetRate.as_view(), name="get_rate"),
    path('suggestion/<int:pk>/', TourSuggestion.as_view(), name="tour_suggestion"),
    path('archivedtl/',ArchivedTourTL.as_view(), name="archived_tour_tl"),
    path('archiveduser/', ArchivedTourUser.as_view(), name="archived_tour_user"),
    # path('searchtour/', SearchTour.as_view(), name="search_tour"),
]
