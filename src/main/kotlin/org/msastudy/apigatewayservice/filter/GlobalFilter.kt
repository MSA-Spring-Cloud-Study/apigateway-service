package org.msastudy.apigatewayservice.filter

import org.msastudy.apigatewayservice.util.Log
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class GlobalFilter : AbstractGatewayFilterFactory<GlobalFilter.Config>(Config::class.java) {
    companion object : Log

    data class Config(
        var baseMessage: String,
        var preLogger: Boolean,
        var postLogger: Boolean
    )

    override fun apply(config: Config): GatewayFilter {
        logger.info("apply used!")
        return GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            val request = exchange.request
            val response = exchange.response

            logger.info("Global Filter baseMessage: {}", config.baseMessage)

            if (config.preLogger) {
                logger.info("Global Filter Start: request id -> {}", request.id)
            }

            chain.filter(exchange).then<Void?>(Mono.fromRunnable {
                logger.info(
                    "Global Filter End: response code -> {}",
                    response.statusCode
                )
            })
        }
    }
}