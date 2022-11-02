package io.getarrays.server.service.implementation;

import io.getarrays.server.enamulation.Status;
import io.getarrays.server.enamulation.repository.ServerRepo;
import io.getarrays.server.model.Server;
import io.getarrays.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

@RequiredArgsConstructor
@Service
@Configuration
@Slf4j
public class ServerServiceImpl implements ServerService {
    private final ServerRepo repository;

    @Override
    public Server create(Server server) {
        log.info("Saving new server: {}", server.getName());
        server.setImageURL(setServerImageURL());
        return repository.save(server);
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging server IP: {} ", ipAddress);
        Server server = repository.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(1) ? Status.SERVER_UP : Status.SERVER_DOWN);
        repository.save(server);
        return server;
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching all services");
        return repository.findAll(PageRequest.of(0, limit)).toList();
    }

    @Override
    public Server get(Long id) {
        log.info("Fetching server by id: {}", id);
        return repository.findById(id).get();
    }

    @Override
    public Server update(Server server) {
        log.info("Updating server: {}", server.getName());
        return repository.save(server);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("Delete server by id: {}", id);
        repository.deleteById(id);
        return Boolean.TRUE;
    }


    private String setServerImageURL() {
        String[] imageNames = {"image1.png", "image2.png", "image3.png", "image4.png"};
        return ServletUriComponentsBuilder.fromCurrentContextPath().
                path("/server/image/" + imageNames[new Random().nextInt(4)]).toUriString();
    }
}
