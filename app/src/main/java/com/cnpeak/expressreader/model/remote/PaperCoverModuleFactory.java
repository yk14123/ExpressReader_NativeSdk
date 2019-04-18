package com.cnpeak.expressreader.model.remote;

import com.cnpeak.expressreader.net.ApiManager;
import com.cnpeak.expressreader.model.bean.PaperCover;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author builder by HUANG JIN on 18/12/26
 * @version 1.0
 * @description 获取报纸封面列表数据
 */
public class PaperCoverModuleFactory {
    public static Observable<List<PaperCover>> getPagerCover(String LCID) {
        return ApiManager.builder().getService().getPagerCover(LCID).subscribeOn(Schedulers.io());
    }
}
