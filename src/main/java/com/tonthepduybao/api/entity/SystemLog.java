package com.tonthepduybao.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * SystemLog
 *
 * @author khal
 * @since 2023/07/15
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ttdb_system_log")
public class SystemLog {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_full_name")
    private String userFullName;

    @Column(name = "access_url", columnDefinition = "TEXT")
    private String accessUrl;

    @Column(name = "time", length = 14, nullable = false)
    private LocalDateTime time;

    @Column(name = "method", length = 20)
    private String method;

    @Column(name = "real_ip_addr", length = 50)
    private String realIpAddr;

    @Column(name = "forwarded_ip_addr", length = 50)
    private String forwardedIpAddr;

    @Column(name = "user_agent", length = 1000)
    private String userAgent;

    @Column(name = "host", length = 1000)
    private String host;

    @Column(name = "locale", length = 1000)
    private String locale;

}
