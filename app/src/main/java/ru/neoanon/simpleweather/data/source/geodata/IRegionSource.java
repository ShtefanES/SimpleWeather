package ru.neoanon.simpleweather.data.source.geodata;

import java.util.List;

import io.reactivex.Observable;
import ru.neoanon.simpleweather.data.source.local.db.location.RegionLocation;

/**
 * Created by eshtefan on  21.09.2018.
 */

public interface IRegionSource {

    Observable<RegionLocation> getCurrentRegionLocation();

    Observable<List<RegionLocation>> getAddressSuggestions(String userRequest);
}
