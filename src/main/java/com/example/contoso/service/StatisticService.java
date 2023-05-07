package com.example.contoso.service;

import com.example.contoso.dto.response.MostActiveBuyersResponse;
import com.example.contoso.dto.response.MostPopularItemResponse;
import com.example.contoso.dto.response.ProfitResponse;
import com.example.contoso.dto.response.FailedSuccessResponse;

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

}
