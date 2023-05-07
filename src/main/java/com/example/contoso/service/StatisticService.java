package com.example.contoso.service;

import com.example.contoso.dto.response.statistic.FailedSuccessResponse;
import com.example.contoso.dto.response.statistic.MostActiveBuyersResponse;
import com.example.contoso.dto.response.statistic.MostPopularItemResponse;
import com.example.contoso.dto.response.statistic.ProfitResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 5/7/2023 7:11 PM
 */
public interface StatisticService {
    List<ProfitResponse> getProfitFor(LocalDate from, LocalDate to);
    List<FailedSuccessResponse> getFailedSuccessOrders(LocalDate from, LocalDate to);
    List<MostActiveBuyersResponse> getTheMostActiveBuyers();
    List<MostPopularItemResponse> getTheMostPopularItem();
}
