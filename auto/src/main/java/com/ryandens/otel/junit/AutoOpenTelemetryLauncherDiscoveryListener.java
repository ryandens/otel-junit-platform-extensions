package com.ryandens.otel.junit;

import com.google.auto.service.AutoService;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.SelectorResolutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.launcher.EngineDiscoveryResult;
import org.junit.platform.launcher.LauncherDiscoveryListener;
import org.junit.platform.launcher.LauncherDiscoveryRequest;

@AutoService(LauncherDiscoveryListener.class)
public final class AutoOpenTelemetryLauncherDiscoveryListener implements LauncherDiscoveryListener {

  private final OpenTelemetryLauncherDiscoveryListener inner;

  public AutoOpenTelemetryLauncherDiscoveryListener() {
    this.inner = new OpenTelemetryLauncherDiscoveryListener(OpenTelemetry.SINGLETON.tracer);
  }

  @Override
  public void launcherDiscoveryStarted(LauncherDiscoveryRequest request) {
    inner.launcherDiscoveryStarted(request);
  }

  @Override
  public void launcherDiscoveryFinished(LauncherDiscoveryRequest request) {
    inner.launcherDiscoveryFinished(request);
  }

  @Override
  public void engineDiscoveryStarted(UniqueId engineId) {
    inner.engineDiscoveryStarted(engineId);
  }

  @Override
  public void engineDiscoveryFinished(UniqueId engineId, EngineDiscoveryResult result) {
    inner.engineDiscoveryFinished(engineId, result);
  }

  @Override
  public void selectorProcessed(
      UniqueId engineId, DiscoverySelector selector, SelectorResolutionResult result) {
    inner.selectorProcessed(engineId, selector, result);
  }
}
