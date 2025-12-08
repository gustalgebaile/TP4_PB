Write-Host "Iniciando deploy ambiente desenvolvimento..."

# Variáveis (ajuste valores reais)
$usuario = "gualg"
$servidor = "192.168.0.123"
$caminhoDestino = "C$\app"  # Caminho usado via SMB

# Caminho local do JAR gerado pelo build
$arquivoLocal = "build\libs\TP3_PB-2.0-SNAPSHOT.jar"

# Copiando arquivo via SMB
Copy-Item -Path $arquivoLocal -Destination "\\$servidor\$caminhoDestino" -Force

# Reiniciar aplicação via PowerShell Remoting
Invoke-Command -ComputerName $servidor -Credential (Get-Credential) -ScriptBlock {
    Restart-Service -Name "biblioteca-app"
    Write-Host "Serviço reiniciado com sucesso."
}

Write-Host "Deploy concluído."