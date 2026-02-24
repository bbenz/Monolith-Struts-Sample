package com.skishop.service;

import com.skishop.model.entity.Campaign;
import com.skishop.repository.CampaignRepository;
import org.springframework.stereotype.Service;

@Service
public class CampaignService extends BaseService<Campaign, String> {
    public CampaignService(CampaignRepository repository) { super(repository); }
}
