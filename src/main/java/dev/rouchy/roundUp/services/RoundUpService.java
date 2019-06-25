package dev.rouchy.roundUp.services;

import dev.rouchy.roundUp.models.RoundUpRequest;
import dev.rouchy.roundUp.models.RoundUpResponse;

public interface RoundUpService {
    RoundUpResponse roundUpCustomerTransactions(RoundUpRequest request);
}
