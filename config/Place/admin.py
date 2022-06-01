from django.contrib import admin
from django_admin_geomap import ModelAdmin, Key

from .models import Place


class Admin(ModelAdmin):
    def change_view(self, request, object_id, form_url='', extra_context=None):
        extra_context = self.set_common(request, extra_context)
        item = list(self.get_queryset(request).filter(pk=int(object_id)))[0]

        if item.geomap_longitude and item.geomap_latitude:
            extra_context[Key.MapItems] = [item]
            extra_context[Key.CenterLongitude] = item.geomap_longitude
            extra_context[Key.CenterLatitude] = item.geomap_latitude
            extra_context[Key.MapZoom] = self.geomap_item_zoom

        return super().changeform_view(request, object_id, form_url, extra_context=extra_context)

    geomap_default_latitude = 32.52104558139624
    geomap_default_longitude = 56.695722841712936
    geomap_default_zoom = 5.5
    geomap_field_longitude = "id_longitude"
    geomap_field_latitude = "id_latitude"


admin.site.register(Place, Admin)