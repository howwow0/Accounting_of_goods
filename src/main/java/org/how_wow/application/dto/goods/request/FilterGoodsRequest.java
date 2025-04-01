package org.how_wow.application.dto.goods.request;

import lombok.Builder;

@Builder
public record FilterGoodsRequest(String searchAllFields, String category) {
}
