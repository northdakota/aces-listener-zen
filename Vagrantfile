Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/trusty64"
  config.vm.provider "virtualbox" do |v|
    v.memory = 4048
    v.cpus = 2
  end
  config.vm.network "forwarded_port", guest: 18231, host: 18231
end