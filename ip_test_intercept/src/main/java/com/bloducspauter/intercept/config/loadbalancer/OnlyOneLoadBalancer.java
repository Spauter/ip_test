package com.bloducspauter.intercept.config.loadbalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;
import java.util.List;

/**
 *
 * 自定义负载均衡
 * @author Bloduc Spauter
 *
 */
@Slf4j
public class OnlyOneLoadBalancer implements ReactorServiceInstanceLoadBalancer {
    String serviceId;
    private final ObjectProvider<ServiceInstanceListSupplier>serviceInstanceListSuppliers;
    public OnlyOneLoadBalancer(ObjectProvider<ServiceInstanceListSupplier>serviceInstanceListSuppliers,String serviceId){
        this.serviceId =serviceId;
        this.serviceInstanceListSuppliers=serviceInstanceListSuppliers;
    }

    public OnlyOneLoadBalancer(ObjectProvider<ServiceInstanceListSupplier>serviceInstanceListSuppliers){
        this.serviceInstanceListSuppliers=serviceInstanceListSuppliers;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier=serviceInstanceListSuppliers.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map((serviceInstances) -> processInstanceResponse(supplier,serviceInstances));
    }

    private Response<ServiceInstance>processInstanceResponse(ServiceInstanceListSupplier supplier,List<ServiceInstance>serviceInstanceList) {
        Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(serviceInstanceList);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
    return serviceInstanceResponse;
    }

    private Response<ServiceInstance>getInstanceResponse(List<ServiceInstance> instanceList){
        log.info("自定义负载");
        if(instanceList.isEmpty()){
            return new EmptyResponse();
        }
        ServiceInstance instance=instanceList.get(0);
        return new DefaultResponse(instance);
    }

    @Override
    public Mono<Response<ServiceInstance>> choose() {
        return ReactorServiceInstanceLoadBalancer.super.choose();
    }
}
